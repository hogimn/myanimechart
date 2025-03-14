const seasons = [
  { season: "winter", months: [1, 2, 3] },
  { season: "spring", months: [4, 5, 6] },
  { season: "summer", months: [7, 8, 9] },
  { season: "fall", months: [10, 11, 12] },
];

export const getCurrentSeason = () => {
  const currentMonth = new Date().getMonth() + 1;
  return seasons.find((season) => season.months.includes(currentMonth))?.season;
};

export const getPreviousSeason = () => {
  const currentSeason = getCurrentSeason();
  const currentIndex = seasons.findIndex(
    (season) => season.season === currentSeason
  );
  const previousIndex =
    currentIndex === 0 ? seasons.length - 1 : currentIndex - 1;
  return seasons[previousIndex].season;
};

export const getNextSeason = () => {
  const currentSeason = getCurrentSeason();
  const currentIndex = seasons.findIndex(
    (season) => season.season === currentSeason
  );
  const nextIndex = currentIndex === seasons.length - 1 ? 0 : currentIndex + 1;
  return seasons[nextIndex].season;
};

export const getCurrentSeasonYear = () => {
  const currentMonth = new Date().getMonth() + 1;
  const currentYear = new Date().getFullYear();
  const currentSeason = seasons.find((season) =>
    season.months.includes(currentMonth)
  );
  return currentSeason?.months[0] <= currentMonth
    ? currentYear
    : currentYear - 1;
};

export const getPreviousSeasonYear = () => {
  const previousSeason = getPreviousSeason();
  const currentYear = new Date().getFullYear();
  return seasons.find((season) => season.season === previousSeason)?.months[0] <
    10
    ? currentYear
    : currentYear - 1;
};

export const getNextSeasonYear = () => {
  const nextSeason = getNextSeason();
  const currentYear = new Date().getFullYear();
  return seasons.find((season) => season.season === nextSeason)?.months[0] <= 3
    ? currentYear + 1
    : currentYear;
};

export const formatDate = (isoString) => {
  if (!isoString) {
    return "Not Yet";
  }
  const date = new Date(isoString);
  return date.toLocaleString();
};

export const getSeasonIndex = (season) =>
  seasons.findIndex((s) => s.season === season);

export const getPreviousSeasonFromSeason = (season) => {
  const index = getSeasonIndex(season);
  return seasons[index === 0 ? seasons.length - 1 : index - 1].season;
};

export const getNextSeasonFromSeason = (season) => {
  const index = getSeasonIndex(season);
  return seasons[(index + 1) % seasons.length].season;
};

export const getPreviousSeasonYearFromYearAndSeason = (year, season) => {
  return season === "winter" ? year - 1 : year;
};

export const getNextSeasonYearFromYearAndSeason = (year, season) => {
  return season === "fall" ? year + 1 : year;
};
