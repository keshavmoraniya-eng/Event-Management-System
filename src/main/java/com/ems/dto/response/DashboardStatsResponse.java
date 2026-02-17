package com.ems.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsResponse {
    private Long totalUsers;
    private Long totalEvents;
    private Long totalBookings;
    private Long totalRevenue;
    private Long activeEvents;
    private Long upcomingEvents;
    private Long completedEvents;
    private Long cancelledBookings;
    private Long confirmedBookings;
    private BigDecimal todayRevenue;
    private Long todayBookings;
    private Long monthlyBookings;
}
