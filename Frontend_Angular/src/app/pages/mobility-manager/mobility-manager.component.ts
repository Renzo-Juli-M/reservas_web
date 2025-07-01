import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { MobilityDTO, MobilityService } from '../../services/mobility/mobility.service';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CurrencyPipe } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MobilityDialogComponent } from './mobility-dialog/mobility-dialog.component';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-mobility-manager',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatTableModule,
    MatSlideToggleModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatSortModule,
    FlexLayoutModule
  ],
  templateUrl: './mobility-manager.component.html',
  styleUrls: ['./mobility-manager.component.css']
})
export class MobilityManagerComponent implements OnInit, AfterViewInit {
  mobilityList: MobilityDTO[] = [];
  dataSource = new MatTableDataSource<MobilityDTO>([]);
  displayedColumns: string[] = ['plate', 'type', 'capacity', 'available', 'actions'];
  isLoading = true;

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private mobilityService: MobilityService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadMobility();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  loadMobility(): void {
    this.isLoading = true;
    this.mobilityService.findAll().subscribe({
      next: (list) => {
        this.mobilityList = list;
        this.dataSource.data = list;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.showError('Error al cargar unidades');
      }
    });
  }

  toggleAvailability(m: MobilityDTO): void {
    this.mobilityService.updateAvailability(m.idMobility, !m.available).subscribe({
      next: () => {
        m.available = !m.available;
        this.showSuccess('Disponibilidad actualizada');
      },
      error: () => this.showError('Error al actualizar')
    });
  }

  deleteMobility(id: number): void {
    if (confirm('¿Deseas eliminar esta unidad?')) {
      this.mobilityService.delete(id).subscribe({
        next: () => {
          this.loadMobility();
          this.showSuccess('Unidad eliminada');
        },
        error: () => this.showError('No se pudo eliminar')
      });
    }
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(MobilityDialogComponent, { width: '400px' });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadMobility();
        this.showSuccess('Unidad registrada');
      }
    });
  }

  private showSuccess(msg: string): void {
    this.snackBar.open(msg, 'Cerrar', { duration: 3000 });
  }

  private showError(msg: string): void {
    this.snackBar.open(msg, 'Cerrar', { duration: 3000, panelClass: ['error-snackbar'] });
  }
}
