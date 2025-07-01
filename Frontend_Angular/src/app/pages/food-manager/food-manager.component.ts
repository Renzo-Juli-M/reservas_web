import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { FoodDTO, FoodService } from '../../services/food/food.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CurrencyPipe } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FoodDialogComponent } from './food-dialog/food-dialog.component';

// ✅ CORRECTOS: Importar módulos individuales desde sus paquetes
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-food-manager',
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
    FlexLayoutModule,
    CurrencyPipe
  ],
  templateUrl: './food-manager.component.html',
  styleUrls: ['./food-manager.component.css']
})
export class FoodManagerComponent implements OnInit, AfterViewInit {
  foods: FoodDTO[] = [];
  displayedColumns: string[] = ['name', 'price', 'available', 'actions'];
  dataSource = new MatTableDataSource<FoodDTO>([]);
  isLoading = true;

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private foodService: FoodService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadFoods();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  loadFoods(): void {
    this.isLoading = true;
    this.foodService.findAll().subscribe({
      next: (foods) => {
        this.foods = foods;
        this.dataSource.data = foods;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.showError('Error al cargar los alimentos');
      }
    });
  }

  toggleAvailability(food: FoodDTO): void {
    this.foodService.updateAvailability(food.idFood, !food.available).subscribe({
      next: () => {
        food.available = !food.available;
        this.showSuccess('Disponibilidad actualizada');
      },
      error: () => this.showError('Error al actualizar disponibilidad')
    });
  }

  deleteFood(id: number): void {
    if (confirm('¿Estás seguro de eliminar este alimento?')) {
      this.foodService.delete(id).subscribe({
        next: () => {
          this.loadFoods();
          this.showSuccess('Alimento eliminado');
        },
        error: () => this.showError('Error al eliminar el alimento')
      });
    }
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(FoodDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadFoods();
        this.showSuccess('Alimento creado');
      }
    });
  }

  private showSuccess(msg: string): void {
    this.snackBar.open(msg, 'Cerrar', { duration: 3000 });
  }

  private showError(msg: string): void {
    this.snackBar.open(msg, 'Cerrar', { duration: 3000 });
  }
}
