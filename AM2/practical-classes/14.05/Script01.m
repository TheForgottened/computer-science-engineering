clc
clear

strF=input('f(x,y)=','s');
f=@(x,y) eval(vectorize(strF));
YN=isHarmonica(f)

[x,y]=meshgrid(-2:0.1:2,-2:0.1:2);
z=f(x,y);
figure
surf(x,y,z)
figure
contour(x,y,z)
figure
mesh(x,y,z)





