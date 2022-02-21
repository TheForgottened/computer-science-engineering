% M�todo de Euler para ED/PVI.
% Vers�o melhorada.
% Este � chamado de melhorado pois possui "aloca��o" de mem�ria.
%   y = NEuler(f, a, b, n, y0) M�todo num�rico para a resolu��o de um PVI
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
%   y(i + 1) = y(i) + h * f(t(i), y(i)) , i = 0, 1, ..., n-1
%
%   N/A - Arm�nioCorreia .: armenioc@isec.pt 
%   07/04/2020 - TheForgotten | https://github.com/TheForgottened

function y = NEuler(f, a, b, n, y0)
    h = (b - a) / n;
    t = a: h: b;

    y = zeros(1, n + 1);

    y(1) = y0;

    for i = 1: n
        y(i + 1) = y(i) + h * f(t(i), y(i));
    end
end
