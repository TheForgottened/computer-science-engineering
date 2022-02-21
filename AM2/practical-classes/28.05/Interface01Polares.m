clc
clear

rho = sqrt(2)
theta = pi/4

[x, y] = Polares2Cartesianas(rho, theta)

rho1 = 2;
theta = 0: 0.1: 2*pi;
rho2 = 2 * 2 * cos(theta);
rho3 = 4 * sin(theta);

figure;
polarplot(theta, rho1);

figure;
polarplot(theta, rho2);
hold on;
polarplot(theta, rho3);
polarplot(theta, -rho2);
polarplot(theta, -rho3);
hold off;

rho4 = 1 + cos(theta);
rho5 = 1 + sin(theta);

figure;
polarplot(theta, rho4);
hold on;
polarplot(theta, rho5);
polarplot(theta, -rho4);
polarplot(theta, -rho5);
hold off;