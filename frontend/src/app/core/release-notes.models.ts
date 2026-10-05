export type ReleaseStatus = 'DRAFT' | 'REVIEW' | 'PUBLISHED';

export interface Application {
  id: number;
  name: string;
}

export interface ReleaseNoteItem {
  id: number;
  title: string;
  description: string | null;
  status: ReleaseStatus;
  application: Application;
  createdAt: string;
  updatedAt: string;
}

export interface CreateReleaseNoteItem {
  applicationId: number;
  title: string;
  description?: string;
}
