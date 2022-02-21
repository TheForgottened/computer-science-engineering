% Fun��o que imprime uma tabela.
% Esta fun��o � espec�fica para o problema em quest�o.
%
% INPUT:
%   yExata - matriz com os valores exatos
%   yAprox - matriz com os valores aproximados
%   yErro - matriz com os valores do erro do m�todo de aproxima��o usado
%   a - in�cio do intervalo de itera��o
%   b - fim do intervalo de itera��o
%   n - n�mero de itera��es
%   s - string com o nome do m�todo de aproxima��o usado
%
% Autor: TheForgotten | https://github.com/TheForgottened
% Data: 07/04/2020

function F_ImprimirTabela(yAprox, yExata, yErro, a, b, n, s)
    fprintf('\nTabela de Valores:\n\n');
    legenda = {'t', 'Valor Exato', ['Aprox. por ', s], 'Erro'};
    
    h = (b - a) / n;
    v = a: h: b;
    
    tabela = [v.', yExata.', yAprox.', yErro.'];
    tabela = [legenda; num2cell(tabela)];
    disp(tabela);
end