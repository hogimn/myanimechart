import { getCurrentSeason, getCurrentSeasonYear } from "../../../util/dateUtil";
import PageTemplate from "../../common/template/PageTemplate";
import { useNavigate } from "react-router-dom";
import {
  ArchiveContainer,
  SeasonButton,
  YearLabel,
  YearRow,
} from "./SeasonArchive.style";

const seasons = ["winter", "spring", "summer", "fall"];

const SeasonArchive = () => {
  const navigate = useNavigate();
  const currentYear = getCurrentSeasonYear();
  const currentSeason = getCurrentSeason();
  const seasonIndex = seasons.indexOf(currentSeason.toLowerCase());

  const years = [];
  for (let y = currentYear; y >= 1917; y--) {
    years.push(y);
  }

  return (
    <PageTemplate>
      <ArchiveContainer>
        {years.map((year) => (
          <YearRow key={year}>
            <YearLabel>{year}</YearLabel>
            {seasons.map((season, idx) => {
              if (year === currentYear && idx > seasonIndex) return null;

              return (
                <SeasonButton
                  key={season}
                  onClick={() =>
                    navigate(`/seasonal-anime?year=${year}&season=${season}`)
                  }
                >
                  {season.charAt(0).toUpperCase() + season.slice(1)}
                </SeasonButton>
              );
            })}
          </YearRow>
        ))}
      </ArchiveContainer>
    </PageTemplate>
  );
};

export default SeasonArchive;
