% NEXATASED  Solu��o Exata para SED.
%   [u, v] = NExataSED(strF, strG, a, u0, v0)
%   u' = f(t, u, v), v' = g(t, u, v), t = [a, b], u(a) = u0 e v(a) = v0  
%
% INPUT:
%   strF, strG - strings das fun��es do 2.� membro das Equa��es Diferenciais
%   n - n�mero de subintervalos ou itera��es do m�todo
%   u0, v0 - condi��es iniciais t = a -> u = u0 e v = v0
%
% OUTPUT: 
%   [u, v] - vector das solu��es aproxima��es e da discretiza��o de t
%
%   27/04/2020 - TheForgotten | https://github.com/TheForgottened

function [u, v] = NExataSED(strF, strG, a, u0, v0)
    syms u(t) v(t)
    
    f = subs(str2sym(strF));
    g = subs(str2sym(strG));  

    ode1 = diff(u) == f;
    ode2 = diff(v) == g;
    odes = [ode1; ode2];

    cond1 = u(a) == u0;
    cond2 = v(a) == v0;
    conds = [cond1; cond2];

    [u(t), v(t)] = dsolve(odes, conds);
end