import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize, forkJoin } from 'rxjs';

import { Application, ReleaseNoteItem, ReleaseStatus } from './core/release-notes.models';
import { ReleaseNotesApiService } from './core/release-notes-api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DatePipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  readonly statuses: ReleaseStatus[] = ['DRAFT', 'REVIEW', 'PUBLISHED'];
  readonly form = new FormGroup({
    applicationId: new FormControl<number | null>(null, { validators: [Validators.required] }),
    title: new FormControl('', { validators: [Validators.required, Validators.maxLength(200)], nonNullable: true }),
    description: new FormControl('', { validators: [Validators.maxLength(2000)], nonNullable: true })
  });

  applications: Application[] = [];
  items: ReleaseNoteItem[] = [];
  isLoading = true;
  isSaving = false;
  changingItemIds = new Set<number>();
  errorMessage = '';

  constructor(private readonly api: ReleaseNotesApiService) {}

  ngOnInit(): void {
    this.loadBoard();
  }

  loadBoard(): void {
    this.isLoading = true;
    this.errorMessage = '';
    forkJoin({
      applications: this.api.getApplications(),
      items: this.api.getItems()
    }).pipe(finalize(() => this.isLoading = false))
      .subscribe({
        next: ({ applications, items }) => {
          this.applications = applications;
          this.items = items;
        },
        error: () => this.errorMessage = 'The board could not be loaded. Make sure the API is running.'
      });
  }

  createItem(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    this.isSaving = true;
    this.errorMessage = '';
    this.api.createItem({
      applicationId: value.applicationId!,
      title: value.title.trim(),
      description: value.description.trim() || undefined
    }).pipe(finalize(() => this.isSaving = false))
      .subscribe({
        next: item => {
          this.items = [item, ...this.items];
          this.form.reset({ applicationId: null, title: '', description: '' });
        },
        error: error => this.errorMessage = this.messageFrom(error, 'The entry could not be created.')
      });
  }

  changeStatus(item: ReleaseNoteItem, event: Event): void {
    const status = (event.target as HTMLSelectElement).value as ReleaseStatus;
    if (status === item.status || this.changingItemIds.has(item.id)) {
      return;
    }

    this.changingItemIds.add(item.id);
    this.errorMessage = '';
    this.api.updateStatus(item.id, status)
      .pipe(finalize(() => this.changingItemIds.delete(item.id)))
      .subscribe({
        next: updatedItem => {
          this.items = this.items.map(existing => existing.id === updatedItem.id ? updatedItem : existing);
        },
        error: error => {
          this.errorMessage = this.messageFrom(error, 'The status could not be changed.');
          this.items = [...this.items];
        }
      });
  }

  trackItem(_: number, item: ReleaseNoteItem): number {
    return item.id;
  }

  private messageFrom(error: { error?: { message?: string } }, fallback: string): string {
    return error.error?.message || fallback;
  }
}
