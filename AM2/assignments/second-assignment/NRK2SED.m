% NRK2SED  M�todo de Runge-Kutta de ordem 2 (RK2) para SED.
%   [t, u, v] = NRK2SED(f, g, a, b, n, u0, v0) 
%   u' = f(t, u, v), v' = g(t, u, v), t = [a, b], u(a) = u0 e v(a) = v0  
%
% INPUT:
%   f, g - fun��es do 2.� membro das Equa��es Diferenciais
%   [a, b] - extremos do intervalo da vari�vel independente t
%   n - n�mero de subintervalos ou itera��es do m�todo
%   u0, v0 - condi��es iniciais t = a -> u = u0 e v = v0
%
% OUTPUT: 
%   [t, u, v] - vector das solu��es aproxima��es e da discretiza��o de t
%   u(i + 1) = u(i) + (1/2) * (k1 + k2), i = 0, 1, ..., n - 1
%   v(i + 1) = v(i) + (1/2) * (k1 + k2), i = 0, 1, ..., n - 1
%
%   27/04/2020 - TheForgotten | https://github.com/TheForgottened

function [t, u, v] = NRK2SED(f, g, a, b, n, u0, v0)
    h = (b - a) / n;
    t = a: h: b;
    
    u = zeros(1, n + 1);
    v = zeros(1, n + 1);
    
    u(1) = u0;
    v(1) = v0;
    
    for i = 1: n
        k1_u = h * f(t(i), u(i), v(i));
        k1_v = h * g(t(i), u(i), v(i));
        
        k2_u = h * f(t(i + 1), u(i) + k1_u, v(i) + k1_v);
        k2_v = h * g(t(i + 1), u(i) + k1_u, v(i) + k1_v);
        
        u(i + 1) = u(i) + (1/2) * (k1_u + k2_u);
        v(i + 1) = v(i) + (1/2) * (k1_v + k2_v);
    end
end