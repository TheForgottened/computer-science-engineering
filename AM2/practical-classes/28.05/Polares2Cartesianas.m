function [x, y] = Polares2Cartesianas(rho, theta)

if rho >= 0
    x = rho * cos(theta);
    y = rho * sin(theta);
else
    x = [];
    y = [];
end
end