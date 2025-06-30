import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DashboardStatsDTO {
  totalReservations: number;
  occupancyRate: number;
  totalRevenue: number;
  totalRooms: number;
  occupiedRooms: number;
}

export interface ReservasPorFechaDTO {
  fecha: string;
  total: number;
}
export interface IngresosPorFechaDTO {
  fecha: string;
  ingresoTotal: number;
}



@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private apiUrl = 'http://localhost:8080/entrepreneur'; // Cambia según tu entorno real

  constructor(private http: HttpClient) {}

  getStats(entrepreneurId: number): Observable<DashboardStatsDTO> {
    return this.http.get<DashboardStatsDTO>(`${this.apiUrl}/${entrepreneurId}/dashboard`);
  }
  getIngresosPorFecha(id: number): Observable<IngresosPorFechaDTO[]> {
    return this.http.get<IngresosPorFechaDTO[]>(`${this.apiUrl}/${id}/ingresos-diaria`);
  }

  getReservasPorFecha(entrepreneurId: number): Observable<ReservasPorFechaDTO[]> {
    return this.http.get<ReservasPorFechaDTO[]>(`${this.apiUrl}/${entrepreneurId}/reservas-diaria`);
  }
}
