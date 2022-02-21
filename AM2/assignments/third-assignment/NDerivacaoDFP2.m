% NDerivacaoDFP Deriva��o Num�rica - f�rmula das diferen�as progressivas para 2 pontos
% F�rmula das Diferen�as progressivas
% f'(xi) = (f(x(i + 1)) - f(x(i)) / h
% INPUT:  f - fun��o
%         [a, b] - intervalo de deriva��o
%         h - passo da discretiza��o
%         y - imagens x vs y
% OUTPUT: [x, y] - malha de pontos
%         dydx - derivada de f 
%
%   11/01/2016  Arm�nio Correia   armenioc@isec.pt
%   23/05/2020 - TheForgotten | https://github.com/TheForgottened

function [x, y, dydx] = NDerivacaoDFP2(f, a, b, h, y)
x = a: h: b;
n = length(x);

if nargin == 4
    y = f(x);
end

dydx = zeros(1, n);

for i = 1: (n - 1)
    dydx(i) = (y(i + 1) - y(i)) / h;
end

dydx(n) = (y(n) - y(n - 1)) / h;
end