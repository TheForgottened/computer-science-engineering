% NDerivacaoDFP Deriva��o Num�rica - f�rmula da segunda derivada para 3 pontos
% F�rmula da Segunda Derivada
% f'(xi) = (f(x(i + 1)) -2 * f(x(i)) + f(x(i - 1)) / h^2
% INPUT:  f - fun��o
%         [a, b] - intervalo de deriva��o
%         h - passo da discretiza��o
%         y - imagens x vs y
% OUTPUT: [x, y] - malha de pontos
%         dydx - derivada de f 
%
%   11/01/2016  Arm�nio Correia   armenioc@isec.pt
%   23/05/2020 - TheForgotten | https://github.com/TheForgottened

function [x, y, dydx] = NDerivacaoDF2D(f, a, b, h, y)
x = a: h: b;
n = length(x);

if nargin == 4
    y = f(x);
end

dydx = zeros(1, n);

for i = 2: (n - 1)
    dydx(i) = (y(i + 1) - 2 * y(i) + y(i - 1)) / h^2;
end

dydx(1) = (y(3) - 2 * y(2) + y(1)) / h^2;
dydx(n) = (y(n) - 2 * y(n - 1) + y(n - 2)) / h^2;
end

