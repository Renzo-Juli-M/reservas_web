import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

export interface RoomDTO {
  idRoom: any;
  number: string;
  type: string;
  price: number;
  available: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class RoomsService {
  private apiUrl = 'http://localhost:8080'; // Base URL
  private roomsSubject = new BehaviorSubject<RoomDTO[]>([]);
  rooms$ = this.roomsSubject.asObservable();

  constructor(private http: HttpClient) { }

  // Get all rooms
  findAll(): Observable<RoomDTO[]> {
    return this.http.get<RoomDTO[]>(`${this.apiUrl}/rooms`).pipe(
      tap(rooms => {
        this.roomsSubject.next(rooms);
      }),
      catchError(this.handleError)
    );
  }

  findById(id: number): Observable<RoomDTO> {
    return this.http.get<RoomDTO>(`${this.apiUrl}/rooms/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  save(room: RoomDTO): Observable<any> {
    return this.http.post(`${this.apiUrl}/rooms`, room, { observe: 'response' }).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, room: RoomDTO): Observable<RoomDTO> {
    return this.http.put<RoomDTO>(`${this.apiUrl}/rooms/${id}`, room).pipe(
      catchError(this.handleError)
    );
  }

  updateAvailability(id: number, available: boolean): Observable<RoomDTO> {
    return this.http.put<RoomDTO>(`${this.apiUrl}/rooms/${id}/availability`, null, {
      params: { available: available.toString() }
    }).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/rooms/${id}`).pipe(
      tap(() => {
        const currentRooms = this.roomsSubject.value;
        const updatedRooms = currentRooms.filter(room => room.idRoom !== id);
        this.roomsSubject.next(updatedRooms);
      }),
      catchError(this.handleError)
    );
  }

  findAvailableRooms(checkInDate: string | Date, checkOutDate: string | Date): Observable<RoomDTO[]> {
    const checkIn = this.formatDate(checkInDate);
    const checkOut = this.formatDate(checkOutDate);

    console.log(`Buscando habitaciones disponibles entre ${checkIn} y ${checkOut}`);

    const url = `${this.apiUrl}/reservations/available`;

    const params = new HttpParams()
      .set('checkIn', checkIn)
      .set('checkOut', checkOut);

    console.log(`Request URL: ${url}`);
    console.log(`Request params:`, params.toString());

    return this.http.get<RoomDTO[]>(url, { params }).pipe(
      tap(rooms => {
        console.log(`Se encontraron ${rooms.length} habitaciones disponibles:`, rooms);
      }),
      catchError(error => {
        console.error('Error al buscar habitaciones disponibles:', error);
        return this.handleError(error);
      })
    );
  }

  private formatDate(date: string | Date): string {
    if (!date) {
      return '';
    }

    if (date instanceof Date) {
      // Format as YYYY-MM-DD
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    } else if (typeof date === 'string') {
      if (/^\d{4}-\d{2}-\d{2}$/.test(date)) {
        return date;
      }
      return this.formatDate(new Date(date));
    }

    console.error('Invalid date format:', date);
    return '';
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Unknown error occurred';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      console.error('Server response:', error.error);
    }

    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
