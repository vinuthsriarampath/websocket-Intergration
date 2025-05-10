import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  constructor(private readonly http: HttpClient) {}

  getChatHistory(user1: string, user2: string): Observable<any> {
    return this.http.get(`http://localhost:8080/history/${user1}/${user2}`);
  }
}
