export interface Message{
  id?: number;
  sender: string;
  recipient: string;
  content: string;
  timestamp?: string;
  status?: string;
}
