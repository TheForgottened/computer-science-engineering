function YN=isHarmonica(f)
syms x y;
%if (diff(f,x,2)+diff(diff(f,y),y)==0)
if (diff(f,x,2)+diff(f,y,2)==0)
    YN=1;
else
    YN=0;
end