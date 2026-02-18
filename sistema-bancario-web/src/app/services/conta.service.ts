import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

// Interface que espelha o seu DTO/Entity do Java
export interface Conta {
  id?: number;
  titular: string;
  numero: string;
  saldo: number;
}

@Injectable({
  providedIn: 'root'
})
export class ContaService {
  private http = inject(HttpClient);
  private readonly API = '/api/contas';

  listar(): Observable<Conta[]> {
    return this.http.get<Conta[]>(this.API);
  }

  buscarPorNumero(numero: string): Observable<any> {
    return this.http.get<any>(`${this.API}/numero/${numero}`);
  }

  buscarPorId(id: number): Observable<Conta> {
    return this.http.get<Conta>(`${this.API}/${id}`);
  }

  criar(conta: Partial<Conta>): Observable<Conta> {
    return this.http.post<Conta>(this.API, conta);
  }

  creditar(id: number, valor: number): Observable<void> {
    return this.http.post<void>(`${this.API}/${id}/creditar`, { valor });
  }

  debitar(id: number, valor: number): Observable<void> {
    return this.http.post<void>(`${this.API}/${id}/debitar`, { valor });
  }

  emitirExtrato(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API}/${id}/extrato`);
  }

  transferir(origemId: number, destinoId: number, valor: number): Observable<any> {
    return this.http.post(`${this.API}/transferir`, {
      origemId: origemId,
      destinoId: destinoId,
      valor: valor
    });
  }

}
