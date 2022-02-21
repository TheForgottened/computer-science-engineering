% M�todo de Euler Melhorado para ED/PVI.
% Este � tamb�m chamado de m�todo de Heun.
%   y = NEuler_M(f, a, b, n, y0) M�todo num�rico para a resolu��o de um PVI
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
%   y(i + 1) = y(i) + h * f(t(i), y(i)), i = 0, 1, ..., n-1
%   y(i + 1) = y(i) + (h/2) * (f(t(i), y(i)) + f(t(i + 1), y(i + 1))),
%   i = 0, 1, ..., n - 1
%
%   N/A - Arm�nioCorreia .: armenioc@isec.pt 
%   07/04/2020 - TheForgotten | https://github.com/TheForgottened

function y = NEuler_M(f, a, b, n, y0)
    h = (b - a) / n;
    t = a: h: b;
    y(1) = y0;
    
    for i = 1: n
        y(i + 1) = y(i) + h * f(t(i), y(i));
        y(i + 1) = y(i) + (h/2) * (f(t(i), y(i)) + f(t(i + 1), y(i + 1)));
    end
end
