% NDerivacaoDFP Deriva��o Num�rica - f�rmula das diferen�as regressivas para 2 pontos
% F�rmula das Diferen�as regressivas
% f'(xi) = (f(x(i)) - f(x(i - 1)) / h
% INPUT:  f - fun��o
%         [a, b] - intervalo de deriva��o
%         h - passo da discretiza��o
%         y - imagens x vs y
% OUTPUT: [x, y] - malha de pontos
%         dydx - derivada de f 
%
%   11/01/2016  Arm�nio Correia   armenioc@isec.pt
%   23/05/2020 - TheForgotten | https://github.com/TheForgottened

function [x, y, dydx] = NDerivacaoDFR2(f, a, b, h, y)
x = a: h: b;
n = length(x);

if nargin == 4
    y = f(x);
end

dydx = zeros(1, n);

for i = n: -1: 2
    dydx(i) = (y(i) - y(i - 1)) / h;
end

dydx(1) = (y(2) - y(1)) / h;

end

