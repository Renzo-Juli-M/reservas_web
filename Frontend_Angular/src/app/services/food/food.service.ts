import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

export interface FoodDTO {
  idFood?: number; // Hacemos el ID opcional
  name: string;
  price: number;
  available: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class FoodService {
  private apiUrl = 'http://localhost:8080/foods';
  private foodSubject = new BehaviorSubject<FoodDTO[]>([]);
  foods$ = this.foodSubject.asObservable();

  constructor(private http: HttpClient) {}

  findAll(): Observable<FoodDTO[]> {
    return this.http.get<FoodDTO[]>(this.apiUrl).pipe(
        tap(foods => this.foodSubject.next(foods)),
        catchError(this.handleError)
    );
  }

  findById(id: number): Observable<FoodDTO> {
    return this.http.get<FoodDTO>(`${this.apiUrl}/${id}`).pipe(catchError(this.handleError));
  }

  save(food: Omit<FoodDTO, 'idFood'>): Observable<any> {
    return this.http.post(`${this.apiUrl}`, food, { observe: 'response' }).pipe(
        catchError(this.handleError)
    );
  }

  update(id: number, food: FoodDTO): Observable<FoodDTO> {
    return this.http.put<FoodDTO>(`${this.apiUrl}/${id}`, food).pipe(
        catchError(this.handleError)
    );
  }

  updateAvailability(id: number | undefined, available: boolean): Observable<FoodDTO> {
    const body = { available };  // Enviar el parámetro en el cuerpo
    return this.http.put<FoodDTO>(`${this.apiUrl}/${id}/availability`, body).pipe(
        catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
        tap(() => {
          const current = this.foodSubject.value;
          this.foodSubject.next(current.filter(f => f.idFood !== id));
        }),
        catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let msg = 'Error desconocido';
    if (error.error instanceof ErrorEvent) {
      msg = `Error: ${error.error.message}`;
    } else {
      msg = `Código: ${error.status}\nMensaje: ${error.message}`;
    }
    console.error('[FoodService Error]:', msg);
    return throwError(() => new Error(msg));
  }
}
