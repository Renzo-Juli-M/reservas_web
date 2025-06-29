import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Angular Material Imports
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSelectModule } from '@angular/material/select';

import { RoomDTO, RoomsService } from '../../../services/room/rooms.service';
import { ReservationDTO, ReservationsService } from '../../../services/reservation/reservations.service';

@Component({
  selector: 'app-reservation-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule,
    MatSelectModule
  ],
  templateUrl: './reservation-dialog.component.html',
  styleUrl: './reservation-dialog.component.css'
})
export class ReservationDialogComponent implements OnInit {
  reservation: ReservationDTO = {
    customerName: '',
    checkInDate: null,
    checkOutDate: null,
    roomId: null
  };

  availableRooms: RoomDTO[] = [];
  isLoading = false;
  minDate: Date;
  areDatesSelected = false;

  constructor(
    private dialogRef: MatDialogRef<ReservationDialogComponent>,
    private roomsService: RoomsService,
    private reservationService: ReservationsService,
    private snackBar: MatSnackBar
  ) {
    this.minDate = new Date();
    this.minDate.setHours(0, 0, 0, 0);
  }

  ngOnInit() {}

  onDateRangeChange() {
    console.log('onDateRangeChange called', this.reservation.checkInDate, this.reservation.checkOutDate);

    if (this.reservation.checkInDate && this.reservation.checkOutDate) {
      // Validate dates
      if (this.reservation.checkOutDate <= this.reservation.checkInDate) {
        const nextDay = new Date(this.reservation.checkInDate);
        nextDay.setDate(nextDay.getDate() + 1);
        this.reservation.checkOutDate = nextDay;
      }

      this.areDatesSelected = true;
      this.loadAvailableRooms();
    } else {
      this.areDatesSelected = false;
      this.availableRooms = [];
      this.reservation.roomId = null;
    }
  }

  loadAvailableRooms() {
    debugger
    if (!this.reservation.checkInDate || !this.reservation.checkOutDate) {
      this.snackBar.open('Por favor seleccione fechas de entrada y salida', 'Cerrar', { duration: 3000 });
      return;
    }

    this.isLoading = true;
    this.availableRooms = []; // Limpiar anteriores

    const checkInDate = this.formatDate(this.reservation.checkInDate);
    const checkOutDate = this.formatDate(this.reservation.checkOutDate);

    console.log(`Buscando habitaciones disponibles: Entrada ${checkInDate}, Salida ${checkOutDate}`);

    this.roomsService.findAvailableRooms(checkInDate, checkOutDate).subscribe({

      next: (rooms) => {
        console.log(`Habitaciones encontradas: ${rooms.length}`);
        this.availableRooms = rooms;

        if (rooms.length === 0) {
          this.snackBar.open('No hay habitaciones disponibles para las fechas seleccionadas', 'Cerrar', { duration: 5000 });
        }

        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al buscar habitaciones disponibles:', err);
        this.snackBar.open('Error al cargar habitaciones: ' + (err.message || 'Error desconocido'), 'Cerrar', { duration: 5000 });
        this.isLoading = false;
      }
    });
  }

  formatDate(date: any): string {
    if (!date) return '';

    if (date instanceof Date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }

    if (typeof date === 'string') {
      if (/^\d{4}-\d{2}-\d{2}$/.test(date)) {
        return date;
      }
      try {
        const dateObj = new Date(date);
        return this.formatDate(dateObj);
      } catch (e) {
        console.error('Error al convertir fecha:', date);
        return '';
      }
    }

    console.error('Formato de fecha no válido:', date);
    return '';
  }

  isFormComplete(): boolean {
    return !!(
      this.reservation.customerName?.trim() &&
      this.reservation.checkInDate &&
      this.reservation.checkOutDate &&
      this.reservation.roomId
    );
  }

  onSubmit() {
    if (this.isFormComplete()) {
      this.isLoading = true;
      this.reservationService.save(this.reservation).subscribe({
        next: (createdReservation) => {
          this.snackBar.open('Reservación creada con éxito', 'Cerrar', { duration: 3000 });
          this.dialogRef.close(createdReservation);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error creando reservación', err);
          this.snackBar.open('Error al crear reservación', 'Cerrar', { duration: 3000 });
          this.isLoading = false;
        }
      });
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
