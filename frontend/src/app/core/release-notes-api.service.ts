import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Application, CreateReleaseNoteItem, ReleaseNoteItem, ReleaseStatus } from './release-notes.models';

@Injectable({ providedIn: 'root' })
export class ReleaseNotesApiService {
  private readonly apiBase = '/api';

  constructor(private readonly http: HttpClient) {}

  getApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiBase}/applications`);
  }

  getItems(): Observable<ReleaseNoteItem[]> {
    return this.http.get<ReleaseNoteItem[]>(`${this.apiBase}/items`);
  }

  createItem(item: CreateReleaseNoteItem): Observable<ReleaseNoteItem> {
    return this.http.post<ReleaseNoteItem>(`${this.apiBase}/items`, item);
  }

  updateStatus(id: number, status: ReleaseStatus): Observable<ReleaseNoteItem> {
    return this.http.patch<ReleaseNoteItem>(`${this.apiBase}/items/${id}/status`, { status });
  }
}
