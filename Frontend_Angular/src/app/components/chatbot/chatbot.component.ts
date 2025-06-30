import { Component } from '@angular/core';
import { ChatbotService } from '../../services/chatbot/chatbot.service';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';


@Component({
  selector: 'app-chatbot',
  standalone: true,

  templateUrl: './chatbot.component.html',
  imports: [
    FormsModule, CommonModule
  ],

  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent {
  userInput = '';
  chatHistory: { from: 'user' | 'bot'; message: string }[] = [];

  constructor(private botService: ChatbotService) {}

  sendMessage() {
    if (!this.userInput.trim()) return;

    const input = this.userInput;
    this.chatHistory.push({ from: 'user', message: input });

    this.botService.sendMessage(input).subscribe(response => {
      this.chatHistory.push({ from: 'bot', message: response.message });

      if (response.roomReserved) {
        this.chatHistory.push({
          from: 'bot',
          message: `Habitación reservada: Nº ${response.roomReserved.number} - ${response.roomReserved.type} - S/.${response.roomReserved.price}`
        });
      }
    });

    this.userInput = '';
  }
}
