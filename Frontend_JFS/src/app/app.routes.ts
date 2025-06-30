import { Routes } from '@angular/router';
import { RoomManagerComponent } from './pages/room-manager/room-manager.component';
import { ReservationManagerComponent } from './pages/reservation-manager/reservation-manager.component';
import {ChatbotComponent} from './components/chatbot/chatbot.component';

export const routes: Routes = [
  { path: 'room-manager', component: RoomManagerComponent },
  { path: 'reservation-manager', component: ReservationManagerComponent },
  { path: 'chatbot', component: ChatbotComponent }

];
