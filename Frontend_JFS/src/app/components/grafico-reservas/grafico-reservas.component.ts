import { Component, Input, inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ChartConfiguration, ChartType } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';
import { PLATFORM_ID } from '@angular/core'; // ← esta es la corrección

@Component({
  selector: 'app-grafico-reservas',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './grafico-reservas.component.html',
  styleUrls: ['./grafico-reservas.component.css']
})
export class GraficoReservasComponent {
  @Input() labels: string[] = [];
  @Input() data: number[] = [];

  isBrowser = isPlatformBrowser(inject(PLATFORM_ID)); // ← ya funciona correctamente

  public barChartType: ChartType = 'bar';

  public get barChartData(): ChartConfiguration<'bar'>['data'] {
    return {
      labels: this.labels,
      datasets: [
        {
          data: this.data,
          label: 'Reservas',
          backgroundColor: '#3f51b5'
        }
      ]
    };
  }
}
