export interface FriendRelation {
  id: number;
  user1Id: number;
  user2Id: number;
  status: 'pending' | 'accepted' | 'rejected';
  createdAt: string;
}
