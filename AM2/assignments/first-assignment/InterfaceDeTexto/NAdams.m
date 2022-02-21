% M�todo de 2 passos Adams�Bashforth para ED/PVI.
% Neste m�todo come�amos por deduzir o y(2) pelo m�todo de Euler pois,
% visto que ele anda de "2 em 2", n�o tem maneira de o calcular.
%   y = NAdams(f, a, b, n, y0) M�todo num�rico para a resolu��o de um PVI
%   y' = f(t, y) com t = [a, b] e y(a) = y0 condi��o inicial  
%
% INPUT:
%   f - fun��o do 2.� membro da Equa��o Diferencial
%   [a, b] - extremos do intervalo da vari�vel independente
%   n - n�mero de subintervalos ou itera��es do m�todo
%   y0 - condi��o inicial t = a -> y = y0
%
% OUTPUT: 
%   y - vector das solu��es aproximadas
%   y(i + 2) = y(i + 1) + (3/2) * h * f(t(i + 1), y(i + 1)) - (1/2) * h * f(t(i), y(i)),
%   i = 0, 1, ..., n-1
%
%   07/04/2020 - TheForgotten | https://github.com/TheForgottened

function y = NAdams(f, a, b, n, y0)
    h = (b - a) / n;
    t = a: h: b;
    y(1) = y0;
    y(2) = y(1) + h * f(t(1), y(1));
    
    for i = 1: n - 1
        y(i + 2) = y(i + 1) + (3/2) * h * f(t(i + 1), y(i + 1)) - (1/2) * h * f(t(i), y(i));
    end
end