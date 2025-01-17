export const toTypeLabel = (str) => {
    switch (str) {
        case 'all':
            return 'ALL'
        case 'tv':
            return 'TV';
        case 'ona':
            return 'ONA';
        case 'movie':
            return 'Movie';
        case 'music':
            return 'Music';
        case 'pv':
            return 'PV';
        case 'tv_special':
            return 'TV Special';
        default:
            return str;
    }
}

export const toAirStatusLabel = (str) => {
    switch (str) {
        case 'all':
            return 'ALL';
        case 'currently_airing':
            return 'Airing'
        case 'finished_airing':
            return 'Ended';
        default:
            return str;
    }
}

export const capitalizeFirstLetter = (str) => {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

export const toEpisodeLabel = (str) => {
    if (str === 0) {
        return "Unknown";
    }
    return str;
}

export const toScoreLabel = (str) => {
    return str.toFixed(2);
}
