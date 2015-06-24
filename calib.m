figure;
vector = five_down;

ax(1) = subplot(311);
plot(vector(:,1),'r');
grid on

ax(2) = subplot(312);
plot(vector(:,2),'g');
grid on

ax(3) = subplot(313);
plot(vector(:,3),'b');
grid on