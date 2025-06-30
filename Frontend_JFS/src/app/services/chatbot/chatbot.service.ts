import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface RoomDTO {
  idRoom: number;
  number: string;
  type: string;
  price: number;
  available: boolean;
}

export interface BotResponse {
  message: string;
  roomReserved?: RoomDTO;
}

@Injectable({ providedIn: 'root' })
export class ChatbotService {

  private apiUrl = 'http://localhost:8080/chatbot/message'; // Ajusta si tu backend usa otro puerto

  constructor(private http: HttpClient) {}

  sendMessage(message: string): Observable<BotResponse> {
    return this.http.post<BotResponse>(this.apiUrl, message);
  }
}
