import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ContaService } from '../../services/conta.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="card shadow border-0 mt-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary"><i class="bi bi-bank"></i> Banco Leoncio - Cadastro de contas</h2>
        <button class="btn btn-voltar-home shadow-sm" routerLink="/">
          <i class="bi bi-house-door-fill me-2"></i> Voltar para Home
        </button>
      </div>
      <div class="card-header bg-primary text-white">
        <h4 class="mb-0">Painel de Controle: Cadastro de Clientes</h4>
      </div>
      <div class="card-body p-4">
        <form (submit)="salvar()">
          <div class="row g-3">
            <div class="col-md-5">
              <input type="text" [(ngModel)]="novaConta.titular" name="titular" class="form-control" placeholder="Nome do Titular" required>
            </div>
            <div class="col-md-3">
              <input type="text" [(ngModel)]="novaConta.numero" name="numero" class="form-control" placeholder="Número da Conta" required>
            </div>
            <div class="col-md-3">
              <input type="number" [(ngModel)]="novaConta.saldo" name="saldo" class="form-control" placeholder="Saldo Inicial" required>
            </div>
            <div class="col-md-1">
              <button type="submit" class="btn btn-success w-100">Criar</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .btn-voltar-home {
        background-color: #ffffff;
        border: 2px solid #007bff;
        color: #007bff;
        font-weight: 600;
        border-radius: 8px;
        padding: 8px 18px;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
    }

    .btn-voltar-home:hover {
        background-color: #007bff;
        color: #ffffff;
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
    }

    .card {
        border-radius: 15px;
        overflow: hidden;
    }
  `]
})
export class AdminComponent {
  private service: ContaService = inject(ContaService);
  novaConta = { titular: '', numero: '', saldo: 0 };

  salvar() {
    if (this.novaConta.saldo < 0) {
      alert('Erro: O saldo inicial não pode ser negativo.');
      return;
    }

    this.service.criar(this.novaConta).subscribe({
      next: () => {
        alert('Conta criada com sucesso!');
        this.novaConta = { titular: '', numero: '', saldo: 0 };
      },
      error: (err) => {
        alert('Erro ao criar conta: ' + (err.error?.message || 'Verifique os dados.'));
      }
    });
  }

}
