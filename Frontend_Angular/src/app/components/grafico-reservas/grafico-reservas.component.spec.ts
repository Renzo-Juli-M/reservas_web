import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraficoReservasComponent } from './grafico-reservas.component';

describe('GraficoReservasComponent', () => {
  let component: GraficoReservasComponent;
  let fixture: ComponentFixture<GraficoReservasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GraficoReservasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GraficoReservasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
