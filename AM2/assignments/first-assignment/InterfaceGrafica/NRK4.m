% M�todo de Runge-Kutta de ordem 4 (RK4) para ED/PVI.
%   y = NRK4(f, a, b, n, y0) M�todo num�rico para a resolu��o de um PVI
%   y' = f(t, y) com t = [a, b] e y(a) = y0 condi��o inicial  
%
% INPUT:
%   f - fun��o do 2.� membro da Equa��o Diferencial
%   [a, b] - extremos do intervalo da vari�vel independente
%   n - n�mero de subintervalos ou itera��es do m�todo
%   y0 - condi��o inicial t = a -> y = y0s
%
% OUTPUT: 
%   y - vector das solu��es aproximadas
%   y(i + 1) = y(i) + (1/6) * (k1 + 2 * k2 + 2 * k3 + k4), i = 0, 1, ..., n-1
%
%   N/A - Arm�nioCorreia .: armenioc@isec.pt 
%   07/04/2020 - TheForgotten | https://github.com/TheForgottened

function y = NRK4(f, a, b, n, y0)
    h = (b - a) / n;
    t = a: h: b;
    
    y = zeros(1, n + 1);
    
    y(1) = y0;
    
    for i = 1: n
        k1 = h * f(t(i), y(i));
        k2 = h * f(t(i) + (h/2), y(i) + (1/2) * k1);
        k3 = h * f(t(i) + (h/2), y(i) + (1/2) * k2);
        k4 = h * f(t(i + 1), y(i) + k3);
        
        y(i + 1) = y(i) + (1/6) * (k1 + 2 * k2 + 2 * k3 + k4);
    end
end