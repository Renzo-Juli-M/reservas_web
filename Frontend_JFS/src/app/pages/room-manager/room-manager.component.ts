import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { RoomDTO, RoomsService } from '../../services/room/rooms.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CurrencyPipe, JsonPipe } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { RoomDialogComponent } from './room-dialog/room-dialog.component';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { FlexLayoutServerModule } from '@angular/flex-layout/server';
import { FlexLayoutModule } from '@angular/flex-layout';

@Component({
  selector: 'app-room-manager',
  standalone: true,
  imports: [
    CurrencyPipe,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatTableModule,
    MatSlideToggleModule,
    MatProgressSpinnerModule,
    MatSortModule,
    FlexLayoutServerModule,
    FlexLayoutModule
  ],
  templateUrl: './room-manager.component.html',
  styleUrls: ['./room-manager.component.css']
})
export class RoomManagerComponent implements OnInit, AfterViewInit {
  rooms: RoomDTO[] = [];
  displayedColumns: string[] = ['number', 'type', 'price', 'available', 'actions'];
  isLoading = true;
  dataSource = new MatTableDataSource<RoomDTO>([]);

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private roomsService: RoomsService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
  ) {}

  ngOnInit() {
    this.loadRooms();
  }

  ngAfterViewInit() {
    this.dataSource.sortingDataAccessor = (item, property) => {
      switch(property) {
        case 'number':
          return typeof item.number === 'string' ? parseInt(item.number, 10) : item.number;
        default:
          // @ts-ignore - Para manejar propiedades dinámicas
          return item[property];
      }
    };

    if (this.sort) {
      this.dataSource.sort = this.sort;

      setTimeout(() => {
        if (this.sort) {
          this.sort.active = 'number';
          this.sort.direction = 'asc';
          this.sort.sortChange.emit();
        }
      });
    }
  }

  loadRooms() {
    this.isLoading = true;
    this.roomsService.findAll().subscribe({
      next: (rooms) => {
        this.rooms = rooms;
        this.dataSource.data = rooms;

        // Configurar el orden después de cargar los datos
        if (this.sort) {
          this.dataSource.sort = this.sort;
          this.sort.active = 'number';
          this.sort.direction = 'asc';
          this.sort.sortChange.emit();
        }

        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.showError('Error al cargar las habitaciones');
      }
    });
  }

  toggleAvailability(room: RoomDTO) {
    this.roomsService.updateAvailability(room.idRoom, !room.available).subscribe({
      next: () => {
        room.available = !room.available;
        this.showSuccess('Disponibilidad actualizada');
      },
      error: () => this.showError('Error al actualizar disponibilidad')
    });
  }

  deleteRoom(id: number) {
    if (confirm('¿Estás seguro de eliminar esta habitación?')) {
      this.roomsService.delete(id).subscribe({
        next: () => {
          this.loadRooms();
          this.showSuccess('Habitación eliminada');
        },
        error: () => this.showError('La habitación cuenta con una reservación pendiente')
      });
    }
  }

  private showSuccess(message: string) {
    this.snackBar.open(message, 'Cerrar', { duration: 3000 });
  }

  private showError(message: string) {
    this.snackBar.open(message, 'Cerrar', { duration: 3000 });
  }

  openDialog() {
    const dialogRef = this.dialog.open(RoomDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.showSuccess('Habitación creada exitosamente');
        this.loadRooms();
      }
    });
  }
}
