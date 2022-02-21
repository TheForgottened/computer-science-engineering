function y=N_Euler(f,a,b,n,y0)
% comentários
h=(b-a)/n;
t(1)=a;
y(1)=y0;
for i=1:n
    y(i+1)=y(i)+h*f(t(i),y(i));
    t(i+1)=t(i)+h;
end
