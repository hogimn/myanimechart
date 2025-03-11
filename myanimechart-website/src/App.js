import "./App.css";
import { HashRouter, Navigate, Route, Routes } from "react-router-dom";
import SeasonalAnime from "./components/pages/seasonalanime/SeasonalAnime";
import { registerCharts } from "./components/common/chart/chartRegistration";
import PollCollectionStatus from "./components/pages/pollcollectionstatus/PollCollectionStatus";

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
        <Route
          path={"/poll-collection-status"}
          element={<PollCollectionStatus />}
        />
      </Routes>
    </HashRouter>
  );
}

export default App;
