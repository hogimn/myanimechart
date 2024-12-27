import React, { useState } from "react";
import CommonTabs from "../../../common/basic/CommonTabs";
import SeasonalAnimeList from "./SeasonalAnimeList";
import styled from "styled-components";

const CustomTabs = styled(CommonTabs)`
    .ant-tabs-tab {
        font-size: 20px;
        padding: 12px 24px;
    }
`;

const SeasonalTabs = () => {
    const [sortBy, setSortBy] = useState("score");

    const tabs = [
        {
            key: "1",
            label: "Fall 2024",
            content: (
                <SeasonalAnimeList
                    year={2024}
                    season="fall"
                    sortBy={sortBy}
                    setSortBy={setSortBy}
                />
            ),
        },
        {
            key: "2",
            label: "Winter 2025",
            content: (
                <SeasonalAnimeList
                    year={2025}
                    season="winter"
                    sortBy={sortBy}
                    setSortBy={setSortBy}
                />
            ),
        },
    ];

    return (
        <div>
            <CustomTabs
                tabs={tabs}
                defaultActiveKey="1"
                onChange={(key) => console.log(`Selected Tab: ${key}`)}
            />
        </div>
    );
};

export default SeasonalTabs;
