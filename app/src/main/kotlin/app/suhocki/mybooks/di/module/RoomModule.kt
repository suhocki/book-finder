package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.RoomRepository
import app.suhocki.mybooks.data.room.dao.*
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.di.provider.database.*
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.domain.repository.StatisticsRepository
import toothpick.config.Module

class RoomModule : Module() {
    init {
        bind(BooksDatabase::class.java)
            .toProvider(BooksDatabaseProvider::class.java)
            .providesSingletonInScope()

        bind(BookDao::class.java)
            .toProvider(BookDaoProvider::class.java)
            .providesSingletonInScope()

        bind(BannerDao::class.java)
            .toProvider(BannerDaoProvider::class.java)
            .providesSingletonInScope()

        bind(CategoryDao::class.java)
            .toProvider(CategoryDaoProvider::class.java)
            .providesSingletonInScope()

        bind(StatusStatisticsDao::class.java)
            .toProvider(StatusStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(YearStatisticsDao::class.java)
            .toProvider(YearStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(PublisherStatisticsDao::class.java)
            .toProvider(PublisherStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(AuthorStatisticsDao::class.java)
            .toProvider(AuthorStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(PriceStatisticsDao::class.java)
            .toProvider(PriceStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(ShopInfoDao::class.java)
            .toProvider(ShopInfoDaoProvider::class.java)
            .providesSingletonInScope()

        bind(BooksRepository::class.java)
            .withName(Room::class.java)
            .to(RoomRepository::class.java)
            .singletonInScope()

        bind(InfoRepository::class.java)
            .withName(Room::class.java)
            .to(RoomRepository::class.java)
            .singletonInScope()

        bind(StatisticsRepository::class.java)
            .to(RoomRepository::class.java)
            .singletonInScope()

        bind(BannersRepository::class.java)
            .to(RoomRepository::class.java)
            .singletonInScope()
    }
}