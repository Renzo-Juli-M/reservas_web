import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe, DecimalPipe } from '@angular/common';
import { DashboardService, DashboardStatsDTO, ReservasPorFechaDTO } from '../../services/emprededor/dashboard.service';
import { GraficoReservasComponent } from '../../components/grafico-reservas/grafico-reservas.component';

@Component({
  selector: 'app-dashboard-emprendedor',
  standalone: true,
  imports: [
    CommonModule,
    CurrencyPipe,
    DecimalPipe,
    GraficoReservasComponent
  ],
  templateUrl: './dashboard-emprendedor.component.html',
  styleUrls: ['./dashboard-emprendedor.component.css']
})
export class DashboardEmprendedorComponent implements OnInit {

  stats: DashboardStatsDTO | null = null;
  entrepreneurId = 1; // ⚠️ Reemplaza con ID dinámico si tienes login

  labels: string[] = [];
  series: number[] = [];

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getStats(this.entrepreneurId).subscribe(data => {
      this.stats = data;
    });

    this.dashboardService.getReservasPorFecha(this.entrepreneurId).subscribe(datos => {
      this.labels = datos.map(d => this.formatearFecha(d.fecha));
      this.series = datos.map(d => d.total);
    });
  }

  formatearFecha(fechaStr: string): string {
    const fecha = new Date(fechaStr);
    return fecha.toLocaleDateString('es-PE', { weekday: 'short', day: 'numeric' });
  }
}
