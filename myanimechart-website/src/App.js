import "./App.css";
import { HashRouter, Navigate, Route, Routes } from "react-router-dom";
import SeasonalAnime from "./components/pages/SeasonalAnime";
import { registerCharts } from "./components/common/chart/chartRegistration";
import PollCollectionStatus from "./components/pages/PollCollectionStatus";
import SeasonArchive from "./components/pages/SeasonArchive";

registerCharts();

function App() {
  return (
    <HashRouter>
      <Routes>
        <Route
          path={"/"}
          element={<Navigate to={"/seasonal-anime"} replace />}
        />
        <Route path={"/seasonal-anime"} element={<SeasonalAnime />} />
        <Route path={"/season-archive"} element={<SeasonArchive />} />
        <Route
          path={"/poll-collection-status"}
          element={<PollCollectionStatus />}
        />
      </Routes>
    </HashRouter>
  );
}

export default App;
