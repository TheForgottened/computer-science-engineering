% NDerivacaoDFP Deriva��o Num�rica - f�rmula das diferen�as regressivas para 3 pontos
% F�rmula das Diferen�as regressivas
% f'(xi) = (f(x(i - 2)) -4 * f(x(i - 1) + 3 * f(x(i))) / (h * 2)
% INPUT:  f - fun��o
%         [a, b] - intervalo de deriva��o
%         h - passo da discretiza��o
%         y - imagens x vs y
% OUTPUT: [x, y] - malha de pontos
%         dydx - derivada de f 
%
%   11/01/2016  Arm�nio Correia   armenioc@isec.pt
%   23/05/2020 - TheForgotten | https://github.com/TheForgottened

function [x, y, dydx] = NDerivacaoDFR3(f, a, b, h, y)
x = a: h: b;
n = length(x);

if nargin == 4
    y = f(x);
end

dydx = zeros(1, n);

for i = n: -1: 3
    dydx(i) = (y(i - 2) -4 * y(i - 1) + 3 * y(i)) / (2 * h);
end

dydx(2) = (y(1) -4 * y(2) + 3 * y(3)) / (2 * h);
dydx(1) = (y(1) -4 * y(2) + 3 * y(3)) / (2 * h);
end

