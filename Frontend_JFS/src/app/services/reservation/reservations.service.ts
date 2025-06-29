import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interfaz que refleja el ReservationDTO de Java
export interface ReservationDTO {
  idReservation?: number;
  customerName: string;
  checkInDate: string | Date | any;
  checkOutDate: string | Date | any;
  roomId: any;
}

@Injectable({
  providedIn: 'root'
})
export class ReservationsService {
  private apiUrl = 'http://localhost:8080/reservations';

  constructor(private http: HttpClient) { }

  findAll(): Observable<ReservationDTO[]> {
    return this.http.get<ReservationDTO[]>(this.apiUrl);
  }

  findById(id: number): Observable<ReservationDTO> {
    return this.http.get<ReservationDTO>(`${this.apiUrl}/${id}`);
  }

  save(reservation: ReservationDTO): Observable<any> {
    const formattedReservation = {
      ...reservation,
      checkInDate: this.formatDate(reservation.checkInDate),
      checkOutDate: this.formatDate(reservation.checkOutDate)
    };
    return this.http.post<any>(this.apiUrl, formattedReservation, { observe: 'response' });
  }

  update(id: number, reservation: ReservationDTO): Observable<ReservationDTO> {
    // Convertir fechas a formato ISO si son objetos Date
    const formattedReservation = {
      ...reservation,
      checkInDate: this.formatDate(reservation.checkInDate),
      checkOutDate: this.formatDate(reservation.checkOutDate)
    };
    return this.http.put<ReservationDTO>(`${this.apiUrl}/${id}`, formattedReservation);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { observe: 'response' });
  }

  private formatDate(date: string | Date): string {
    if (date instanceof Date) {
      return date.toISOString().split('T')[0]; // Formato YYYY-MM-DD
    }
    return date;
  }


}
