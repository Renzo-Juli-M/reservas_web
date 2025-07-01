import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

export interface MobilityDTO {
  idMobility?: number; // Ahora es opcional
  type: 'CAR' | 'BUS';
  plate: string;
  capacity: number;
  available: boolean;
}

@Injectable({ providedIn: 'root' })
export class MobilityService {
  private apiUrl = 'http://localhost:8080/mobility';
  private mobilitySubject = new BehaviorSubject<MobilityDTO[]>([]);
  mobility$ = this.mobilitySubject.asObservable();

  constructor(private http: HttpClient) {}

  findAll(): Observable<MobilityDTO[]> {
    return this.http.get<MobilityDTO[]>(this.apiUrl).pipe(
      tap(data => this.mobilitySubject.next(data)),
      catchError(this.handleError)
    );
  }

  findById(id: number): Observable<MobilityDTO> {
    return this.http.get<MobilityDTO>(`${this.apiUrl}/${id}`).pipe(catchError(this.handleError));
  }

  // Ya no exige un idMobility en el objeto
  save(mobility: Omit<MobilityDTO, 'idMobility'>): Observable<any> {
    return this.http.post(this.apiUrl, mobility, { observe: 'response' }).pipe(catchError(this.handleError));
  }

  update(id: number, mobility: MobilityDTO): Observable<MobilityDTO> {
    return this.http.put<MobilityDTO>(`${this.apiUrl}/${id}`, mobility).pipe(catchError(this.handleError));
  }

  updateAvailability(id: number | undefined, available: boolean): Observable<MobilityDTO> {
    return this.http.put<MobilityDTO>(`${this.apiUrl}/${id}/availability`, null, {
      params: new HttpParams().set('available', available.toString())
    }).pipe(catchError(this.handleError));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => {
        const updated = this.mobilitySubject.value.filter(m => m.idMobility !== id);
        this.mobilitySubject.next(updated);
      }),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const msg = error.error instanceof ErrorEvent
      ? `Error: ${error.error.message}`
      : `Error Code: ${error.status}\nMessage: ${error.message}`;
    console.error('[MobilityService Error]:', msg);
    return throwError(() => new Error(msg));
  }
}
