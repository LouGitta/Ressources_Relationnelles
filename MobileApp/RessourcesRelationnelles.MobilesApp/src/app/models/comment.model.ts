export interface Comment {
  id: number;
  content: string;
  userId: number;
  ressourceId: number;
  parentId?: number | null;
  createdAt: string;
}
