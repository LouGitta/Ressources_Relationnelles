export interface Ressource {
  id: number;
  title: string;
  content: string;
  views: number;
  userId: number;
  relationId: number;
  typeId: number;
  categoryId: number;
  visibility: 'private_visibility' | 'shared' | 'public_visibility';
  status: 'pending' | 'published' | 'rejected';
  createdAt: string;
}
