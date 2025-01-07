import React, {useEffect, useState} from "react";
import CommonTabs from "../../../common/basic/CommonTabs";
import SeasonalAnimeList from "./SeasonalAnimeList";
import styled from "styled-components";

const CustomTabs = styled(CommonTabs)`
    .ant-tabs-tab + .ant-tabs-tab {
        margin: 0 0 0 0;
    }
    
    .ant-tabs-tab {
        font-size: 17px;
        padding: 12px 24px;
    }
`;

const SeasonalTabs = () => {
    const [sortBy, setSortBy] = useState("score");
    const [filterBy, setFilterBy] = useState({type: "all", airStatus: "all"});
    const [activeTab, setActiveTab] = useState("1");
    const [page, setPage] = useState(1);
    const pageSize = 12;

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
                    filterBy={filterBy}
                    setFilterBy={setFilterBy}
                    page={page}
                    setPage={setPage}
                    pageSize={pageSize}
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
                    filterBy={filterBy}
                    setFilterBy={setFilterBy}
                    page={page}
                    setPage={setPage}
                    pageSize={pageSize}
                />
            ),
        },
    ];

    const handleTabChange = (key) => {
        setActiveTab(key);
        setPage(1);
    };

    const goToTop = () => {
        window.scrollTo(0, 0);
    };

    useEffect(() => {
        goToTop();
    }, [page]);

    useEffect(() => {
        setPage(1);
        goToTop();
    }, [filterBy]);

    return (
        <div>
            <CustomTabs
                tabs={tabs}
                defaultActiveKey="1"
                activeKey={activeTab}
                onChange={handleTabChange} // Call handleTabChange on tab change
            />
        </div>
    );
};

export default SeasonalTabs;