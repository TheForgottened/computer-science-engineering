function [yh yp y] = solveODEL2(p,b)
%SOLVEODEL2  Resolver Equações Diferenciais Lineares de Ordem 2 Completa
%   Aplicando o Método da Variação das Constantes Arbitrárias
%   a2*y''+a1*y'+a0*y = b(x)  
%
%INPUT:
%   p - Vector com os Coeficientes da Equação Característica p=[a2 a1 a0]
%   b - Vector coluna com os termos independentes do Sistema de Lagrange 
%       b=[0; b(x)/a2] 
%    
%OUTPUT: 
%   yh - Solução geral da Equação Homogénea Correspondente a2*y''+a1*y'+a0*y = 0 
%   yp - Uma solução particular da ED Completa 
%   y - Solução Geral da Equação Diferencial y = yh + yp
%
%CHAMADA da função na linha de comandos
%   syms x  declarar x como variável simbólica
%   [yh yp y] = solveODEL2([a2 a1 a0],[0; b(x)])
%
%   Autor: Arménio Correia .: armenioc@isec.pt 
%   Data: 17/02/2020

syms x C1 C2 % declaração de variáveis locais e simbólicas

% Caso não sejam introduzidos os parâmetros de entrada p e b
% Considerar por exemplo a ED y''-2y'+y = exp(x)

% if (~nargin) 
%     p = [1 -2 1]; 
%     b = [0; exp(x)];
% end;

if(length(p) ~= 3)
    error('não é de segunda ordem')
else
    % 1.º PASSO
    % Resolução da Equação Homogénea Correspondente
    yh = dsolve([num2str(p(1)) '*D2y +(' num2str(p(2)) ')*Dy +('  ...
             num2str(p(3)) ')*y=0'],'x');
    
    % Determinação das raízes da equação característica a2*x^2+a1*x+a0=0
    s = solve(poly2sym(p));
   
    % Estabelecer o Sistema Fundamental de Soluções S.F.S
    if(p(2)^2-4*p(1)*p(3)) == 0
        for i=1:2
            s(i) = x^(i-1)*exp(s(i)*x); % Raiz real de multiplicidade=2
        end
    elseif (p(2)^2-4*p(1)*p(3)) > 0
        s  = exp(s*x); % Raizes reais simples
    else
        % Raizes complexas
        s =[exp(real(s(1))*x)*sin(abs(imag(s(1)))*x) ...
            exp(real(s(2))*x)*cos(abs(imag(s(2)))*x)].';
    end

    % 2.º PASSO
    % Determinação de uma Solução Particular da Equação Completa

    % Matriz Wronskiana 
    % coincidente com a matriz dos coeficientes do Sistema de Lagrange   
    A=[s.'; simplify(diff(s,x)).'];
    % Wronskiano = determinante da matriz A
    %W = det(A)
   
    % Resolução do Sistema de Lagrange = Sistema de Cramer
    % em ordem às derivadas das constantes arbitrárias 
    C_linha = inv(A)*b;
    % Determinação das constantes arbitrárias por integração
    C  = int(C_linha,x);
   
    % Estabelecer uma solução particular yp da equação completa em que 
    % C1=C1(x) e C2=C2(x), isto é, são funções de x = Constantes Arbitrárias
    yp = sym(0);
    for i=1:length(s)
        yp = yp + eval(['C' num2str(i)])*s(i);
    end
    
    % Substituir em yp C1 e C2 pelos valores obtidos em C  
    for i=1:length(C)
        yp = subs(yp,eval(['C' num2str(i)]),C(i));
    end
    
    % 3.º PASSO
    % Solução ou Integral Geral da Equação Diferencial
    y = yh + yp;
end