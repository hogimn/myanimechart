export const createGradient = (ctx, chartArea, colorStart, colorEnd) => {
  const { top, bottom } = chartArea;
  const gradient = ctx.createLinearGradient(0, top, 0, bottom);
  gradient.addColorStop(0, colorStart);
  gradient.addColorStop(1, colorEnd);
  return gradient;
};
