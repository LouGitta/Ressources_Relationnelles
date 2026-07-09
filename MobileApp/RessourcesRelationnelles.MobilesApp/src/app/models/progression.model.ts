export interface Progression {
  id: number;
  userId: number;
  ressourceId: number;
  favorite: boolean;
  viewCount: number;
  completionPercentage: number;
  lastViewedAt: string;
}
