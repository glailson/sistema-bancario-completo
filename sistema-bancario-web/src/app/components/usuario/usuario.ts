import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ContaService } from '../../services/conta.service';

@Component({
  selector: 'app-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './usuario.html',
  styleUrl: './usuario.css'
})
export class UsuarioComponent implements OnInit {
  idConta: number = 0;
  conta: any = null;
  valorTransacao: number = 0;
  idDestino: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private contaService: ContaService
  ) {}

  ngOnInit(): void {
    const idUrl = this.route.snapshot.paramMap.get('id');
    if (idUrl) {
      this.idConta = Number(idUrl);
      this.buscarConta();
    }
  }

  buscarConta(): void {
    if (!this.idConta || this.idConta <= 0) {
      alert('Por favor, acesse através de uma conta válida.');
      return;
    }

    // Buscando os dados da conta logada para atualizar saldo e extrato
    this.contaService.buscarPorNumero(this.idConta.toString()).subscribe({
      next: (res) => {
        this.conta = res;
        console.log('Dados da conta atualizados:', res);
      },
      error: (err) => {
        console.error('Erro ao buscar conta:', err);
        alert('Erro ao carregar dados da conta.');
        this.conta = null;
      }
    });
  }

  executarOperacao(tipo: 'creditar' | 'debitar'): void {
    if (this.valorTransacao <= 0) {
      alert('Informe un valor válido!');
      return;
    }

    const operacao = tipo === 'creditar'
      ? this.contaService.creditar(this.conta.id, this.valorTransacao)
      : this.contaService.debitar(this.conta.id, this.valorTransacao);

    operacao.subscribe({
      next: () => {
        alert(`Operação de ${tipo.toUpperCase()} realizada com sucesso!`);
        this.valorTransacao = 0;
        this.buscarConta();
      },
      error: (err) => alert('Erro na operação: ' + (err.error?.message || 'Saldo insuficiente'))
    });
  }

  realizarTransferencia(): void {
    if (!this.idDestino || this.valorTransacao <= 0) {
      alert('Informe o número da conta de destino e um valor válido!');
      return;
    }

    // O back-end agora espera: (id da conta logada, número da conta destino, valor)
    this.contaService.transferir(this.conta.id, Number(this.idDestino), this.valorTransacao).subscribe({
      next: () => {
        alert('Transferência realizada com sucesso!');
        this.valorTransacao = 0;
        this.idDestino = null;
        this.buscarConta(); // Atualiza o extrato para mostrar a nova transação
      },
      error: (err) => {
        console.error('Erro na transferência:', err);
        alert(err.error?.message || 'Erro ao realizar transferência. Verifique o número da conta destino.');
      }
    });
  }
}
