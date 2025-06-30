import { Component, OnInit } from '@angular/core';
import { ReservationDTO, ReservationsService } from '../../services/reservation/reservations.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule, DatePipe } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import {FlexLayoutServerModule} from '@angular/flex-layout/server';
import { FlexLayoutModule } from '@angular/flex-layout';
import { ReservationDialogComponent } from './reservation-dialog/reservation-dialog.component';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';

@Component({
  selector: 'app-reservation-manager',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatTableModule,
    MatProgressSpinnerModule,
    FlexLayoutServerModule,
    FlexLayoutModule,
    MatNativeDateModule,
    MatDatepickerModule
  ],
  templateUrl: './reservation-manager.component.html',
  styleUrls: ['./reservation-manager.component.css']
})
export class ReservationManagerComponent implements OnInit {
  reservations: ReservationDTO[] = [];
  displayedColumns: string[] = ['customerName', 'roomId', 'checkInDate', 'checkOutDate', 'actions'];
  isLoading = true;

  constructor(
    private service: ReservationsService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations() {
    this.isLoading = true;
    this.service.findAll().subscribe({
      next: (data) => {
        this.reservations = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.showError('Error al cargar las reservaciones');
      }
    });
  }

  deleteReservation(id: number) {
    const confirmation = confirm('¿Estás seguro de eliminar esta reservación?');
    if (confirmation) {
      this.service.delete(id).subscribe({
        next: () => {
          this.loadReservations();
          this.showSuccess('Reservación eliminada correctamente');
        },
        error: () => this.showError('Error al eliminar la reservación')
      });
    }
  }

  formatDate(date: string | Date): string {
    if (typeof date === 'string') {
      date = new Date(date);
    }
    return date.toLocaleDateString('es-ES');
  }

  private showSuccess(message: string) {
    this.snackBar.open(message, 'Cerrar', { duration: 3000 });
  }

  private showError(message: string) {
    this.snackBar.open(message, 'Cerrar', { duration: 3000 });
  }

    openDialog() {
      const dialogRef = this.dialog.open(ReservationDialogComponent, {
        width: '400px'
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          // Agrega la nueva habitación a la lista actual
          this.reservations.push(result);
          this.showSuccess('Habitación creada exitosamente');
          this.loadReservations()
        }
      });
    }
}
