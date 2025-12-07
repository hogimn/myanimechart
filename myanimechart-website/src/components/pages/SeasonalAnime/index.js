import { useLocation } from "react-router-dom";
import PageTemplate from "../../common/template/PageTemplate";
import SeasonalTabs from "./component/SeasonalTabs";

const SeasonalAnime = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const year = queryParams.get("year");
  const season = queryParams.get("season");

  return (
    <PageTemplate>
      <SeasonalTabs season={season} year={year} />
    </PageTemplate>
  );
};

export default SeasonalAnime;
