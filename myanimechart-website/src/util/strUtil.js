export const toTypeLabel = (str) => {
  switch (str) {
    case "all":
      return "All";
    case "tv":
      return "TV";
    case "ona":
      return "ONA";
    case "movie":
      return "Movie";
    case "music":
      return "Music";
    case "pv":
      return "PV";
    case "tv_special":
      return "TV Special";
    default:
      return str;
  }
};

export const toSourceLabel = (str) => {
  if (!str) return "";
  const newStr = str.replace(/_/g, " ");
  return newStr.charAt(0).toUpperCase() + newStr.slice(1);
};

export const toAirStatusLabel = (str) => {
  switch (str) {
    case "all":
      return "All";
    case "currently_airing":
      return "Airing";
    case "finished_airing":
      return "Ended";
    default:
      return str;
  }
};

export const capitalizeFirstLetter = (str) => {
  return str.charAt(0).toUpperCase() + str.slice(1);
};

export const toEpisodeLabel = (str) => {
  if (str === 0) {
    return "?";
  }
  return str;
};

export const toScoreLabel = (str) => {
  return str.toFixed(2);
};

export const toDateLabel = (str) => {
  if (str == null) {
    return "Unknown";
  }
  return str.split("T")[0];
};

export const toCollectionStatusLabel = (str) => {
  switch (str) {
    case "COMPLETED":
      return "Completed";
    case "WAIT":
      return "Wait";
    case "FAILED":
      return "Failed";
    case "IN_PROGRESS":
      return "InProgress";
    default:
      return str;
  }
};
